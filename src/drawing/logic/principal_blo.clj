(ns drawing.logic.principal-blo
  (:use [drawing.data.principal-dao :as principal-dao]
        [buddy.core.hash :as hash]))

(defn get-principal [login password]
  (let [principal (principal-dao/get-user login (hash/sha3-256 password))]
    (if (nil? principal) nil
                         (dissoc (assoc principal :id (str (:_id principal))) :_id :password))))

(defn register-principal [model]
  (principal-dao/create-user (assoc model :password (hash/sha3-256 (:password model)))))
