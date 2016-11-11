(ns drawing.main
  (:use org.httpkit.server
        [clojure.tools.logging :only [info]]
        [clojure.data.json :only [json-str]]
        (compojure [core :only [defroutes routes]]
                   [handler :as handler]
                   [route :only [files not-found resources]])
        [drawing.routes :as app-routes]
        [drawing.data.events-dao :as events]
        [ring.middleware.cors :refer :all])
  (:require [compojure.handler :as handler]))

(defn- wrap-request-logging [handler]
  (fn [{:keys [request-method uri] :as req}]
    (let [resp (handler req)]
      (info (name request-method) (:status resp)
            (if-let [qs (:query-string req)]
              (str uri "?" qs) uri))
      resp)))

(def app-site (handler/site app-routes/app-routes))

(def app-api (handler/api app-routes/api-routes))

(def app
  (wrap-cors (routes app-api app-site) :access-control-allow-origin [#"http://localhost:3449"]
             :access-control-allow-methods [:get :put :post :delete]))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  (reset! server
          (run-server (-> #'app wrap-request-logging) {:port 9000}))
  (info "Server started on http://127.0.0.1:9000"))
