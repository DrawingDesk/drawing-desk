(ns drawing.controllers.register
  (:use [ring.util.response :as r]
        [ring.middleware.json :only [wrap-json-body]]))

(defn register-index []
  (r/response "Ok"))

(defn register-room [req]
  (r/response req))