(ns drawing.logic.events-blo
  (:use [drawing.data.events-dao :as event-dao]
        [drawing.models.event]))

(def room-counters (agent {}))

(defn- -hide-event-id [items]
  "Remove ObjectId for all items in collection"
  (map (fn [item](dissoc (map->Event item) :_id)) items))

(defn process-new-event [room-id event]
  (map->Event (event-dao/insert-event room-id event)))

(defn get-events
  "Return room events"
  ([room-id]
   "Return all room events"
   (-hide-event-id (event-dao/get-all-events room-id)))
  ([room-id sync-id]
   "Return list of events after event with the following sync-id"
   (-hide-event-id (event-dao/get-events-after room-id sync-id))))
