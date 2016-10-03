(ns drawing.main
  (:use org.httpkit.server
        [clojure.tools.logging :only [info]]
        [clojure.data.json :only [json-str]]
        (compojure [core :only [defroutes GET POST]]
                   [handler :only [site]]
                   [route :only [files not-found resources]])))

(defn index-page [req]
  {:status 200
   :body "Hi, men!"})

(defroutes main-routes
           (GET "/" [] index-page)
           (not-found "Page not found"))

(defn- wrap-request-logging [handler]
  (fn [{:keys [request-method uri] :as req}]
    (let [resp (handler req)]
      (info (name request-method) (:status resp)
            (if-let [qs (:query-string req)]
              (str uri "?" qs) uri))
      resp)))

(defn -main [& args]
  (run-server (-> #'main-routes site wrap-request-logging) {:port 9898})
  (info "Server started on http://127.0.0.1:9898"))
