(ns drawing.controllers.home
  (:use [ring.util.response :as r]))

(defn home-index []
  (r/file-response "index.html" {:root "public"}))