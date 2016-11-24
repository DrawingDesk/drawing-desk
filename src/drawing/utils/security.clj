(ns drawing.utils.security
  (:use [buddy.auth.backends.token :refer [jwe-backend]]
        [buddy.core.nonce :as nonce]
        [buddy.sign.jwt :as jwt]
        [clj-time.core :as time]
        [ring.util.response :as r]
        [buddy.auth.accessrules :only [restrict]]
        [drawing.utils.thread-local :only [thread-local]]))

(defonce secret (nonce/random-bytes 32))

(defonce encoder {:alg :a256kw :enc :a128gcm})

(def empty-principal {:identity nil})

(def principal (thread-local (atom empty-principal)))


(defn unathorized [request metadata]
  (r/status {:body "Unauthorized"} 403))

(def auth-backend (jwe-backend {:secret               secret
                                :options              encoder
                                :unauthorized-handler unathorized}))

(defn is-authenticated? [req]
  (not (= @@principal empty-principal)))

(defn wrap-user [handler]
  (fn [{identity :identity :as req}]
      (if (not (nil? identity))
        (swap! @principal assoc :identity identity)
        (reset! @principal empty-principal))
      (handler req)))

(defn wrap-authentificated? [handler]
  (restrict handler {:handler is-authenticated?}))


(defn generate-token
  ([auth-model]
   (let [claims {:user auth-model
                 :exp  (time/plus (time/now) (time/seconds 3600))}]
     (jwt/encrypt claims secret encoder)))
  ([]
   (let [claims {:user (:user (:identity @@principal))
                 :exp  (time/plus (time/now) (time/seconds 3600))}]
     (jwt/encrypt claims secret encoder))))