(ns drawing.routes
  (:use [drawing.controllers.home :as home]
        [drawing.controllers.register :as register]
        [drawing.controllers.events :as events]
    (compojure [core :only [defroutes GET POST]]
             [handler :only [site]]
             [route :only [files not-found resources]])))
(defroutes app-routes
           (GET "/" [] ( home/home-index))
           (GET "/register" [] (register/register-index))
           (POST "/register" [room-id] (register/register-room room-id))
           (GET "/events/:room-id" [room-id] (events/events-index room-id))
           (not-found "Page not found"))