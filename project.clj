(defproject rchive "0.0.1"
  :dependencies [[io.bloomventures/omni "0.36.2"]
                 ;; why???
                 [org.clojure/tools.cli "1.4.256"]]

  :omni-config rchive.omni-config/omni-config

  :plugins [[io.bloomventures/omni "0.36.2"]]

  :main rchive.core

  :profiles {:dev
             {:source-paths ["dev-src"]}
             :uberjar
             {:aot :all
              :prep-tasks [["omni" "compile"]
                           "compile"]}})

