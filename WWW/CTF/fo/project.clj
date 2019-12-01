(defproject fo "0.1.0-SNAPSHOT"
  :description "与佛论禅"
  :url "https://fo.lolis.cc"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]

                 [com.taoensso/timbre "4.10.0"]
                 [buddy/buddy-core "1.4.0"]
                 [compojure "1.6.1"]
                 [ring "1.7.0"]
                 [aleph "0.4.6"]]

  :resource-paths ["resources/"]
  :ring {:auto-refresh? true :auto-reload? true
         :reload-paths ["src"]
         :refresh-paths ["resources/"]}

  :main fo.core
  :aot [fo.core]
  )
