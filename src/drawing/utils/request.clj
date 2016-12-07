(ns drawing.utils.request
  (:use [ring.middleware.json :only [wrap-json-body wrap-json-response]]
        [ring.middleware.cors :only [wrap-cors]]))


(defn wrap-post-requests [handler]
  (wrap-json-body handler {:keywords? true :bigdecimals? true}))

(defn wrap-json-requests [handler]
  (wrap-json-response handler {}))

(defn wrap-cors-request [handler]
  (wrap-cors handler :access-control-allow-origin #".*" :access-control-allow-methods [:get :put :post :delete :patch]))