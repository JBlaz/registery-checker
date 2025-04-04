(ns core
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [etaoin.api :as e]))


(defn read-json [file-path]
  (json/parse-string (slurp (io/file file-path)) true))

(defn write-results-to-file [file-path results]
  (let [json-data (json/generate-string results {:pretty true})]
    (spit file-path json-data)))

(def state (atom {:first-run true
                  :retest []
                  :tested []}))

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
                              members)]

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

          (Thread/sleep 10000)

          ;; Select "All" from the dropdown to show all results
          (e/select driver {:name "nsopwdt_length"} "All")

          ;; Extract the search results from the div with class "dataTables_info"
          (let [results (e/get-element-text driver {:css ".dataTables_info"})]
            (if (not= results "Showing 0 to 0 of 0 entries")
              (do
                (prn "Found results for" firstName lastName)
                (prn "Results:" results)
                (prn "----------------------------------"))
              (swap! state update :tested conj {:firstName firstName :lastName lastName :results results})))

          ;; Navigate back to the previous page
          (Thread/sleep 1000)
          (e/back driver)))

      (finally
        (write-results-to-file "output/retest.json" (:retest @state))
        (write-results-to-file "output/tested.json" (:tested @state))
        (e/quit driver)))))
