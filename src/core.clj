(ns core
  (:require [etaoin.api :as e]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clj-http.client :as http]))

(def url "https://nsopw-api.ojp.gov/nsopw/v1/v1.0/search")

(def headers
  {"Accept" "application/json, text/javascript, */*; q=0.01"
   "Accept-Language" "en-US,en;q=0.7"
   "Content-Type" "application/x-www-form-urlencoded; charset=UTF-8"
   "Origin" "https://www.nsopw.gov"
   "Referer" "https://www.nsopw.gov/"
   "User-Agent" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36"
   "Sec-Fetch-Dest" "empty"
   "Sec-Fetch-Mode" "cors"
   "Sec-Fetch-Site" "cross-site"
   "Sec-GPC" "1"})

(defn read-json [file-path]
  (json/parse-string (slurp (io/file file-path)) true))

(defn post-request [url payload]
  (http/post url
             {:headers headers
              :body (json/generate-string payload)}))

(defn -main []
  ;; Start a browser session (e.g., Chrome)
  (let [driver (e/chrome)]
    (try
      ;; Navigate to the website
      (e/go driver "https://www.nsopw.gov/")

      ;; Read members from JSON
      (let [members (read-json "assets/members.json")
            member-names (map #(hash-map
                                :firstName (get % :nameGivenPreferredLocal "")
                                :lastName (get % :nameFamilyPreferredLocal ""))
                              members)
            state (atom {:first-run true
                         :retest []
                         :tested []})] ; Track whether it's the first search

        ;; Search for each member on the website
        (doseq [{:keys [firstName lastName]} member-names]

          (Thread/sleep 1000)

          ;; Fill in the first name and last name fields
          (e/clear driver {:id "firstname"})
          (e/clear driver {:id "lastname"})

          ;; Fill in the first name and last name fields
          (e/fill driver {:id "firstname"} firstName)
          (e/fill driver {:id "lastname"} lastName)

          ;; Click the search button
          (e/click driver {:id "searchbyname"})

          ;; Sleep for 5 seconds only for the first search
          (Thread/sleep 10000)

          ;; Extract the search results from the div with class "dataTables_info"
          (let [results (e/get-element-text driver {:css ".dataTables_info"})]
            (if (not= results "Showing 0 to 0 of 0 entries")
              (do
                (println "---------------------")
                (println "Searched for:" firstName lastName)
                (println "Results:" results)
                (swap! state update :retest conj {:firstName firstName :lastName lastName}))
              (swap! state update :tested conj {:firstName firstName :lastName lastName})))

          ;; Navigate back to the previous page
          (Thread/sleep 1000)
          (e/back driver))

          (prn "---------------------")
          (prn "Retest:" (:retest @state)))

      ;; Close the browser
      (finally
        (e/quit driver)))))
