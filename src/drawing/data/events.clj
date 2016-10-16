(ns drawing.data.events
  (:use [drawing.data.provider :as provider]
        [drawing.configuration :as config]
        [monger.collection :as mc]
        [monger.conversion :refer [from-db-object]]))

(defn insert-event [room-id event]
  "Function that insert new event and return inserted object"
  (provider/execute-query (fn [db]
                            (mc/insert-and-return db room-id event))))

(defn get-all-events [room-id]
  "Function retunrns all events from collection"
  (provider/execute-query (fn [db]
                            (sort-by :id
                                     (mc/find-maps db room-id)))))