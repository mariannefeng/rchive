(ns rchive.client.remote
  (:require
   [bloom.commons.ajax :as ajax]
   [reagent.ratom :as ratom]))

(defn ajax-promise! [opts]
  (js/Promise.
   (fn [resolve reject]
     (ajax/request (assoc opts
                          :on-success resolve
                          :on-error reject)))))

(defn tada! [[event-id params]]
  (ajax-promise!
   {:uri (str "/api/tada/" (namespace event-id) "." (name event-id))
    :method :post
    :params {:tada.event/id event-id
             :tada.event/params (or params {})}}))

(defn tada-atom
  [[event-id params] & {:keys [refresh-rate]}]
  (let [ratom (ratom/atom nil)
        f (fn []
            (-> (tada! [event-id params])
                (.then (fn [data]
                         (reset! ratom data)))))
        interval (when refresh-rate (js/setInterval f refresh-rate))]
    (f)
    (ratom/make-reaction (fn []
                           (with-meta
                             @ratom
                             {::refresh-fn f}))
                         :on-dispose
                         (fn []
                           (js/clearInterval interval)))))

(defn refresh! [rx]
  ((::refresh-fn (meta @rx))))

