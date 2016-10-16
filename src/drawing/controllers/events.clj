(ns drawing.controllers.events
      (:use [ring.util.response :as r]))

(defn events-index [room-id]
      (println room-id)
      (r/response room-id))
