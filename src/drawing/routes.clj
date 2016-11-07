(ns drawing.routes
  (:use [drawing.controllers.home :as home]
        [drawing.controllers.register :as register]
        [drawing.controllers.events :as events]
        [drawing.models.event]
        [ring.middleware.keyword-params :refer [wrap-keyword-params]]
        [ring.middleware.json :only [wrap-json-body wrap-json-response]]
        (compojure [core :only [defroutes GET POST ANY context]]
                   [handler :as handler]
                   [route :only [files not-found resources]])))

(defn -wrap-post-requests [handler]
  (wrap-json-body (fn [req]
                    (handler (-> req :body))) {:keywords? true :bigdecimals? true}))

(defn -wrap-json-requests [handler]
  (wrap-json-response handler {}))

(defroutes app-routes
           (GET "/" [] (home/home-index))
           (not-found "Page not found"))

(defroutes api-routes
           (context "/api" []
             (GET "/register" [] (-wrap-json-requests (fn [req](register/register-index))))
             (POST "/register" [] (-wrap-post-requests register/register-room))
             (GET "/events/:room-id" [room-id] (-wrap-json-requests (fn [req](events/events-index room-id))))
             (GET "/events/:room-id/:sync" [room-id sync] (-wrap-json-requests (fn [req](events/events-index room-id sync))))
             (POST "/events/:room-id" [room-id] (-wrap-json-requests (-wrap-post-requests (fn [req]
                                                                                            (events/receive-event room-id (map->Event req))))))))