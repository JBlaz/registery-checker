(defproject my-clojure-app "0.1.0-SNAPSHOT"
  :description "A simple Clojure application"
  :url "http://example.com/my-clojure-app"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main core
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [enlive "1.1.6"]
                 [etaoin "1.0.39"]
                 [cheshire "5.11.0"]
                 [clj-http "3.12.3"]]
  :profiles {:uberjar {:aot :all}}
  :resource-paths ["resources"])