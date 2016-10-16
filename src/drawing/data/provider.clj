(ns drawing.data.provider
  (:use [monger.core :as mg]
        [drawing.configuration :as config]))


(defn execute-query [query]
  (let [conn (mg/connect config/mongodb-connection-string)
        db (mg/get-db conn config/mongodb-db)]
    (query db)))
