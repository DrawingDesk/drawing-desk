(ns drawing.logic.events-blo
  (:use [drawing.data.events-dao :as event-dao]
        [drawing.models.event]
        [drawing.utils.security :only [principal]]
        [drawing.utils.clock :as clock])
  (:import (drawing.models.event Event)))

(defn- -prepare-entity [entity]
  "Remove ObjectId for all items in collection"
  (dissoc (map->Event entity) :_id))

(defn- -prepare-collection [items]
  "Remove ObjectId for all items in collection"
  (map -prepare-entity items))


(defn process-new-event [room-id event]
  (map->Event (-prepare-entity (event-dao/insert-event room-id (assoc event :sync-id (clock/next-value room-id) :user-id (:id (:user (:identity @@principal))))))))

(defn get-events
  "Return room events"
  ([room-id]
   "Return all room events"
   (-prepare-collection (event-dao/get-all-events room-id)))
  ([room-id sync-id]
   "Return list of events after event with the following sync-id"
   (-prepare-collection (event-dao/get-events-after room-id sync-id))))

(defn apply-event-update [room-id sync-id event]
  (event-dao/update-event room-id sync-id event)
  (map->Event (-prepare-entity
                (event-dao/insert-event room-id
                                        (Event.
                                          (clock/next-value room-id) (:id (:user (:identity @@principal))) nil "update-event" {:sync-id sync-id :data event})))))
