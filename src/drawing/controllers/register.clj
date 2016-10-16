(ns drawing.controllers.register
  (:use [ring.util.response :as r]))

(defn register-index []
  (r/response "Ok"))

(defn register-room [room-id]
  (r/response room-id))