(ns core
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]))

(defn parse-html
  [file-path]
  (let [html-content (slurp (io/file file-path))
        parsed-html (html/html-resource (java.io.StringReader. html-content))]
    (for [row (html/select parsed-html [:tr]) ;; Select all rows
          :let [tds (html/select row [:td]) ;; Select all <td> elements in the row
                _ (println "Row content:" (map html/text tds)) ;; Debug: Print all <td> content
                address-cell (nth tds 2 nil) ;; Adjust index based on actual structure
                city-cell (nth tds 3 nil)
                zip-cell (nth tds 4 nil)
                address (when address-cell (string/trim (html/text address-cell)))
                city (when city-cell (string/trim (html/text city-cell)))
                zip (when zip-cell (string/trim (html/text zip-cell)))]
          :when (and address city zip)] ;; Ensure all fields are present
      {:address address
       :city city
       :zip zip})))

(defn -main []
  (let [file-path "assets/page_1.html"
        results (parse-html file-path)]
    (doseq [entry results]
      (println "Address:" (:address entry))
      (println "City:" (:city entry))
      (println "ZIP:" (:zip entry)))))