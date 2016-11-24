(ns drawing.data.principal-dao
  (:refer-clojure :exclude [find sort])
  (:use [drawing.data.provider :as provider]
        [monger.collection :only [find-maps insert-and-return ensure-index]]
        [monger.conversion :refer [from-db-object]]
        [monger.operators :refer :all]
        [monger.query :refer :all]))

(def user-collection "user")

(defn create-user [principal]
  (provider/execute-query (fn [db]
                            (ensure-index db user-collection (array-map :login 1) { :unique true })
                            (insert-and-return db user-collection principal))))

(defn get-user [login password]
  (let [principals (provider/execute-query (fn [db]
                                         (with-collection db user-collection
                                                          (find {:login login :password password})
                                                          (limit 1))))]
    (first principals)))
