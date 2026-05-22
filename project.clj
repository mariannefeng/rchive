(defproject rchive "0.0.1"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.clojure/clojurescript "1.12.134"]
                 [reagent "2.0.1"]
                 [cljsjs/react "18.3.1-1"]
                 [cljsjs/react-dom "18.3.1-1"]]

  :plugins [[lein-figwheel "0.5.18"]]

  :jvm-opts ["--enable-native-access=ALL-UNNAMED"]

  :cljsbuild {:builds [{:id "example"
                        :source-paths ["src/"]
                        :figwheel {:on-jsload "rchive.core/reload"}
                        :compiler {:main "rchive.core"
                                   :asset-path "js/out"
                                   :output-to "resources/public/js/rchive.js"
                                   :output-dir "resources/public/js/out"}}]})