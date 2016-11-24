(ns drawing.controllers.events
  (:use [drawing.logic.events-blo :as events-blo]
        [ring.util.response :as r]))

(defn events-index
  ([room-id]
   "Get all events controller action"
   (r/response (events-blo/get-events room-id)))
  ([room-id sync-id]
   "Get all events after events with sync-id controller action"
   (r/response (events-blo/get-events room-id sync-id))))

(defn receive-event [room-id event]
  (r/response (events-blo/process-new-event room-id event)))