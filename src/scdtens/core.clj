(ns scdtens.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as p]))

(def filename "SCD+Results+S14.csv")

(defn format-key [str-key]
  (when (string? str-key)
    (-> str-key
        clojure.string/lower-case
        (clojure.string/replace #" " "-")
        keyword)))

(defn load-csv-file []
  (let [file-info (csv/read-csv (slurp (io/resource filename)) :quot-char \" :separator \,)
        headers (map format-key (first file-info))]
    (map #(zipmap headers %) (rest file-info))))

(defn get-week-groups-for-judge [k data]
  (group-by :week (filter #(= "10" (k %)) data)))

(defn get-weeks [m]
  (map #(key %) m))

(defn get-min-week [v]
  (->> (get-weeks v)
       (map #(Integer/valueOf %))
       sort
       first))

(defn report-for-judge [w data]
  (filter #(= w (first %)) data))

(defn report-for-week [jk w data]
  (map #(select-keys % [:series :week :order jk :couple]) (data w)))
