(ns drawing.routes
  (:use [drawing.controllers.home :as home]
        [drawing.controllers.register :as register]
        [drawing.controllers.events :as events]
        [ring.middleware.keyword-params :refer [wrap-keyword-params]]
        [ring.middleware.json :only [wrap-json-body]]
        (compojure [core :only [defroutes GET POST ANY context]]
                   [handler :as handler]
                   [route :only [files not-found resources]])))

(defn -wrap-post-requests [handler]
  (wrap-json-body (fn [req]
                    (handler (-> req :body))) {:keywords? true :bigdecimals? true}))

(defroutes app-routes
           (GET "/" [] (home/home-index))
           (not-found "Page not found"))

(defroutes api-routes
           (context "/api" []
             (GET "/register" [] (register/register-index))
             (POST "/register" [] (-wrap-post-requests register/register-room))
             (GET "/events/:room-id" [room-id] (events/events-index room-id))))