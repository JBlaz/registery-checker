(ns simple-check
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.string :as s]))

(defn parse-pedo [file-path]
  (let [data (json/parse-string (slurp (io/file file-path)) true)]
    (map (fn [offender]
           {:name (str
                  ;;  (s/lower-case (get-in offender [:name :givenName] ""))
                  ;;      " "
                  ;;      (s/lower-case (get-in offender [:name :middleName] ""))
                  ;;      " "
                   (s/lower-case (get-in offender [:name :surName] "")))
            :addresses (map (fn [location]
                              (s/lower-case (get location :streetAddress "")))
                            (get offender :locations))})
         (get data :offenders))))

(defn parse-members
  [file-path]
  (let [data (json/parse-string (slurp (io/file file-path)) true)]
    (->> data
         (map (fn [member]
                {:name (s/lower-case (str
                                    ;;   (get-in member [:nameFormats :givenPreferredLocal] "")
                                    ;;       " "
                                      (get-in member [:nameFormats :familyPreferredLocal] "")))
                 :address (s/lower-case (get-in member [:address :formattedLine1] ""))})))))

(defn find-common
  [main other]
  (let [main-names (group-by :name main)] ; Group members by name for efficient lookup
    (reduce (fn [acc offender]
              (if-let [matches (get main-names (:name offender))]
                (concat acc (map (fn [match] [match offender]) matches)) ; Combine matching pairs
                acc))
            []
            other)))

(defn -main []
  (let [members (parse-members "assets/members.json")
        pedos (parse-pedo "assets/us.json")
        combined (find-common members pedos)]
;;     (doseq [pedo (take 5 pedos)]
;;       (println "Name:" (:name pedo))
;;       (println "Address:" (:addresses pedo))
;;       (println "-----------------------"))
;;     (doseq [member (take 5 members)]
;;       (prn "Name:" (:name member))
;;       (prn "Address:" (:address member))
;;       (prn "-----------------------"))
    (doseq [result combined]
      (println "Member:" (first result))
      (println "Offender:" (second result))
      (println "-----------------------"))))