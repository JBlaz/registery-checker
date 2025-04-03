(ns core
  (:require [clojure.java.io :as io]
            [net.cgrand.enlive-html :as html]))

(defn parse-html [file-path]
  (let [html-content (slurp (io/file file-path))
        parsed-html (html/html-resource (java.io.StringReader. html-content))]
    (for [row (html/select parsed-html [:tr])
          :let [name (first (html/select row [:strong]))
                address (first (html/select row [:td :br]))]
          :when (and name address)]
      {:name (html/text name)
       :address (html/text address)})))

(defn -main []
  (let [file-path "c:/Users/Jase/Documents/code/registery-checker/src/page_1.html"
        results (parse-html file-path)]
    (doseq [entry results]
      (println "Name:" (:name entry) "Address:" (:address entry)))))