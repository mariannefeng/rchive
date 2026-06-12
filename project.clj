(defproject rchive "0.0.1"
  :dependencies [[io.bloomventures/omni "0.36.2"]
                 [io.bloomventures/commons "0.17.1"]
                 [cljsjs/qrcode-generator "1.4.4-0"]
                 [ring-oauth2 "0.3.0"]
                 [tada "0.3.0"
                  :exclusions [org.clojure/clojure
                               metosin/malli
                               org.clojure/spec.alpha]]
                 [clj-jgit "1.1.0" :exclusions [org.eclipse.jgit/org.eclipse.jgit.gpg.bc]]
                 ;; why???
                 [org.clojure/tools.cli "1.4.256"]]

  :omni-config rchive.omni-config/omni-config-hack

  :plugins [[io.bloomventures/omni "0.36.2"]]

  :main rchive.core

  :profiles {:dev
             {:source-paths ["dev-src"]}
             :uberjar
             {:aot :all
              :prep-tasks [["omni" "compile"]
                           "compile"]}})

