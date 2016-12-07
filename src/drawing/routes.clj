(ns drawing.routes
   (:use [drawing.controllers.home :as home]
         [drawing.controllers.auth :as auth]
         [drawing.controllers.events :as events]
         [drawing.models.event]
         [drawing.models.auth]
         [drawing.models.user]
         [drawing.utils.security :as security]
         (compojure [core :only [defroutes GET POST ANY PATCH context]]
                    [route :only [files not-found resources]])))

(defroutes app-routes
           (GET "/" [] (home/home-index))
           (POST "/sign-in" [] (fn [req] (auth/login (map->Auth (:body req)))))
           (POST "/sign-up" [] (fn [req] (auth/register (map->User (:body req)))))
           (POST "/refresh" [] (security/wrap-authentificated? (fn [req] (auth/refresh))))
           (not-found "Page not found"))

(defroutes secured-api-routes
           (GET "/events/:room-id" [room-id] (fn [req]
                                               (events/events-index room-id)))
           (GET "/events/:room-id/:sync" [room-id sync] (fn [req]
                                                          (events/events-index room-id sync)))
           (POST "/events/:room-id" [room-id] (fn [req]
                                                (events/receive-event room-id (map->Event (:body req)))))
           (POST "/events/:room-id/:id" [room-id id] (fn [req]
                                                        (println req)
                                                        (events/patch-event room-id id (:body req)))))

(defroutes api-routes
           (context "/api" []
             (security/wrap-authentificated? secured-api-routes)))