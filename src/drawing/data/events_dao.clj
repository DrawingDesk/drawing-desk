(ns drawing.data.events-dao
  (:use [drawing.data.provider :as provider]
        [monger.collection :as mc]
        [monger.conversion :refer [from-db-object]]
        [monger.operators :refer :all]))

(defn insert-event [room-id event]
  "Function that insert new event and return inserted object"
  (provider/execute-query (fn [db]
                            (mc/insert-and-return db room-id event))))

(defn get-all-events [room-id]
  "Function returns all events from collection"
  (provider/execute-query (fn [db]
                            (sort-by :sync-id
                                     (mc/find-maps db room-id)))))

(defn get-events-after [room-id event-id]
  "Function returns all events that occured after event with id event-id"
  (provider/execute-query (fn [db]
                            (sort-by :sync-id
                                     (mc/find-maps db room-id {:sync-id {"$gt" event-id}})))))