(ns drawingdesk.core)

(defn handler [request]
  {:status  200
   :headers {"content-type" "text/html"}
   :body    "Hello World!"})