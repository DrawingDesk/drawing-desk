(ns drawing.controllers.register
  (:use [ring.util.response :as r]
        [ring.middleware.json :only [wrap-json-body]]))

(defn register-index []
  (r/response "Ok"))

(defn register-room [req]
  (let [room (-> req :params :room)]
    (println room))
  (println req)
  (r/response req))