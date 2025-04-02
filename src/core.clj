(ns core
  (:require [clj-http.client :as client]
            [etaoin.api :as e]))

;; (defn fetch-data [url]
;;   (try
;;     (let [response (client/get url
;;                                {:headers {"User-Agent" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
;;                                           "Accept" "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
;;                                           "Referer" "https://www.google.com"}
;;                                 :as :string
;;                                 :throw-exceptions false})]
;;       (if (= 403 (:status response))
;;         (do (println "What? " response)
;;             "Error: Access forbidden (403)")
;;         (:body response)))
;;     (catch Exception e
;;       (str "Error fetching data: " (.getMessage e)))))

;; (defn -main
;;   "The entry point for the application."
;;   [& args]
;;   (println "Hello, World!")
;;   (println "Arguments:" args)
;;   (println (fetch-data ""))) ;; Print the raw string

(def driver (e/chrome))

(defn fetch-with-browser [url]
  (e/go driver url)
  (e/click driver [{:}])
  )

(defn -main [& args]
  (println "Fetching data from example API...")
  (println (fetch-with-browser "http://www.communitynotification.com/cap_office_disclaimer.php?office=54438")))