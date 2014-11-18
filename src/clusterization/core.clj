(ns clusterization.core
  (:gen-class)
  (:require [clojure.java.io :as io]))


(defn my-func
  [line]
  (vector line))

(defn read-file
  [file-name]
  (with-open [rdr (io/reader file-name)]
    (doall (map my-func (line-seq rdr)))))
  ;(with-open [rdr (io/reader file-name)]
  ;  (doseq [line (line-seq rdr)]
  ;    (println line))))

(defn -main
  "I don't do a whole lot ... yet."
  [file-name & the-rest]
  (println file-name)
  (read-file file-name))
