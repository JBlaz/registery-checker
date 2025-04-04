(ns utah
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
                    ;;     _ (println "Row content:" (map html/text tds)) ;; Debug: Print all <td> content
                name-cell (nth tds 4 nil) ;; Adjust index based on actual structure
                address-cell (nth tds 5 nil)
                city-cell (nth tds 6 nil)
                zip-cell (nth tds 7 nil)
                name (when name-cell (string/trim (html/text name-cell)))
                address (when address-cell (string/trim (html/text address-cell)))
                city (when city-cell (string/trim (html/text city-cell)))
                zip (when zip-cell (string/trim (html/text zip-cell)))]
          :when (and name address
                     (seq name)
                     (seq address))] ;; Ensure all fields are present
      {:name name
       :address address
       :city city
       :zip zip})))

(defn init
  []
  (let [file-path "assets/page_1.html"
        results (parse-html file-path)]
    (doseq [entry results]
      (println "Name:" (:name entry))
      (println "Address:" (:address entry))
      (println "City:" (:city entry))
      (println "ZIP:" (:zip entry)))))