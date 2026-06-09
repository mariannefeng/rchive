(ns rchive.client.ui.placard
  (:require
   [rchive.client.ui.rcqr :as rcqr]
   [rchive.client.remote :as remote]))

(defonce config (remote/tada-atom [:api/config]))
(defn url [shortcode]
  (str (:config/qr-base-domain @config)
       "/"
       shortcode))

(defn artists
  [placard]
  [:div {:tw "italic font-light"}
   (interpose ", " (:placard/artists placard))])

(defn title-and-year
  [placard]
  [:div {:tw "text-3xl"}
   [:span {:tw "font-bold"}
    (:placard/title placard) ", "]
   [:span {:tw "font-thin"}
    (:placard/year placard)]])

(defn materials
  [placard]
  [:div {:tw "uppercase text-xs tracking-wider font-light"}
   (:placard/materials placard)])

(defn description
  [placard]
  [:div {:tw "font-light"}
   (:placard/description placard)])

(defn print-cut-markers
  []
  [:<>
   [:div {:tw "border-b border-r w-2 h-2 -top-2 -left-2 absolute hidden print:block"}]
   [:div {:tw "border-t border-r w-2 h-2 -bottom-2 -left-2 absolute hidden print:block"}]
   [:div {:tw "border-t border-l w-2 h-2 -bottom-2 -right-2 absolute hidden print:block"}]
   [:div {:tw "border-b border-l w-2 h-2 -top-2 -right-2 absolute hidden print:block"}]])

(defn placard-horizontal-with-stripe
  [{:keys [show-qr?]} placard]
  [:div#placard
   {:tw "border relative m-4 print:border-transparent break-inside-avoid"}

   [:div {:tw "border-l-1em p-10 box-content bg-white"
          :style {:width "150mm"}}

    [artists placard]
    [:div {:tw "h-4"}]
    [title-and-year placard]
    [:div {:tw "h-1"}]
    [materials placard]

    (when show-qr?
      [:<>
       [:div {:tw "h-4"}]
       [:div {:tw "flex gap-10"}
        [description placard]
        [rcqr/rc-qr-code {:width "4.25rem"
                          :text (url (:placard/shortcode placard))}]]])]

   [print-cut-markers]])

(defn placard-vertical-no-stripe
  [{:keys [show-qr?]} placard]
  [:div#placard
   {:tw "border relative m-4 print:border-transparent break-inside-avoid inline-block"}

   [:div {:tw "p-10 box-content bg-white"
          :style {:width "120mm"}}

    [artists placard]
    [:div {:tw "h-4"}]
    [title-and-year placard]
    [:div {:tw "h-1"}]
    [materials placard]
    [:div {:tw "h-4"}]
    [description placard]

    (when show-qr?
      [:<>
       [:div {:tw "h-20"}]
       [:div {:tw "flex justify-end"}
        [rcqr/rc-qr-code {:width "4rem"
                          :text (url (:placard/shortcode placard))}]]])]
   [print-cut-markers]])

(def placard-view
  #_placard-horizontal-with-stripe
  placard-vertical-no-stripe)


