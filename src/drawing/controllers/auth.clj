(ns drawing.controllers.auth
  (:use [ring.util.response :as r]
        [drawing.utils.security :as security]
        [drawing.logic.principal-blo :as principal-blo])
  (:import (com.mongodb DuplicateKeyException)))

(defn login [auth-model]
  "login controller action"
  (let [principal (principal-blo/get-principal (:login auth-model) (:password auth-model))]
    (if (nil? principal)
      (r/status {:body "User is not found"} 403)
      (let [token (security/generate-token principal)]
        (r/response {:token token :user principal})))))

(defn register [user-model]
  "register controller action"
  (try
    (let [principal (principal-blo/register-principal user-model)
          token (security/generate-token principal)]
      (r/response {:token token}))
    (catch DuplicateKeyException e (r/status {:body "Login is not unique"} 403))))

(defn refresh []
  "refresh token"
  (let [token (security/generate-token)]
    (r/response {:token token})))



