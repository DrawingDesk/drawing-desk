(ns drawing.main
  (:use org.httpkit.server
            [clojure.tools.logging :only [info]]
            [clojure.data.json :only [json-str]]
            (compojure [core :only [defroutes routes]]
                       [handler :as handler]
                       [route :only [files not-found resources]])
            [drawing.routes :as app-routes]
            [drawing.utils.security :as security]
            [drawing.utils.request :as request]
            [ring.middleware.cors :refer :all]
            [buddy.auth.middleware]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [compojure.handler :as handler]))

(defn- wrap-request-logging [handler]
  (fn [req]
    (println req)
    (handler req)))

(def app-site (handler/site app-routes/app-routes))

(def app-api (handler/api app-routes/api-routes))


(def app
  (-> (routes app-api app-site)
      (security/wrap-user)
      (wrap-request-logging)
      (wrap-authorization security/auth-backend)
      (wrap-authentication security/auth-backend)
      (request/wrap-post-requests)
      (request/wrap-json-requests)
      (request/wrap-cors-request)))


(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  (reset! server
          (run-server app {:port 9000}))
  (info "Server started on http://127.0.0.1:9000"))
