(ns rchive.rcapi
  (:require
   [org.httpkit.client :as http]
   [cheshire.core :as json]))

(defn me [token]
  (when token
    (-> @(http/request
          {:url "https://www.recurse.com/api/v1/people/me"
           :oauth-token token})
        :body
        (json/parse-string keyword))))

(def memo-me
  (memoize me))