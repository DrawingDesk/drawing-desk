(ns drawing.data.events-dao
  (:refer-clojure :exclude [find sort])
  (:use [clojure.core :only [read-string]]
        [drawing.data.provider :as provider]
        [monger.collection :only [find-maps insert-and-return]]
        [monger.conversion :refer [from-db-object]]
        [monger.operators :refer :all]
        [monger.query :refer :all]))

(defn insert-event [room-id event]
  "Function that insert new event and return inserted object"
  (provider/execute-query (fn [db]
                            (insert-and-return db room-id event))))

(defn get-all-events [room-id]
  "Function returns all events from collection"
  (provider/execute-query (fn [db]
                            (sort-by :sync-id
                                     (find-maps db room-id)))))

(defn get-events-after [room-id sync-id]
  "Function returns all events that occured after event with id sync-id"
  (provider/execute-query (fn [db]
                            (println room-id sync-id)
                            (sort-by :sync-id
                                     (find-maps db room-id {:sync-id {"$gt" (read-string sync-id)}})))))
(defn get-last-event [room-id]
  (let [events (provider/execute-query (fn [db]
                                         (with-collection db room-id
                                                          (find {})
                                                          (fields [:sync-id :_id])
                                                          (sort (array-map :sync-id -1))
                                                          (limit 1))))]
    (first events)))