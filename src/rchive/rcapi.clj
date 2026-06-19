(ns rchive.rcapi
  (:require
   [org.httpkit.client :as http]
   [cheshire.core :as json]))

(defn me [token]
  (when token
    (let [response (-> @(http/request
                         {:url "https://www.recurse.com/api/v1/people/me"
                          :oauth-token token})
                       :body
                       (json/parse-string keyword))]
      (when (:id response)
        response))))

(def memo-me
  (memoize me))
