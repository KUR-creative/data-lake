(ns data-lake.sqlite-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]
            [orchestra.spec.test :as st]
            [clojure.edn :as edn]
            [data-lake.task.sqlite :refer :all]))


(st/instrument)

(def schema-map
  {:file
   [[:guid      :BLOB    "NOT NULL" "PRIMARY KEY"]
    [:path      :TEXT    "NOT NULL"]
    [:extension :TEXT    "NOT NULL"]
    [:size      :INTEGER "NOT NULL"]]
   :annotation
   [[:input  :BLOB "NOT NULL"] ;guid
    [:output :BLOB "NOT NULL"] ;guid
    [:method :TEXT "NOT NULL" 
     :REFERENCES "annotation_method(name)"]]})

(deftest create!-test
  (testing "create sqlite db file"
    (let [path "./test/fixture/test.db" 
          file (io/as-file path)
          _    (create! path {:file [[:test :INTEGER]
                                     [:chk  :BLOB]]})]
      (do (is (.exists file)) (.delete file))))
  (testing "create sqlite db file"
    (let [path "./test/fixture/test2.db" 
          file (io/as-file path)
          _    (create! path schema-map)]
      (do (is (.exists file)) 
          (.delete file))))
  (testing "if successful create! db then return db-spec"
    (let [path "./test/fixture/test3.db" 
          file (io/as-file path)
          db   (create! path schema-map)]
      (do (is (.exists file)) 
          (is (= {:classname   "org.sqlite.JDBC"
                  :subprotocol "sqlite"
                  :subname     path}
                 db))
          (.delete file)))))

(def edn-path "./DB/sqlite/szmc_schema_0.1.0.edn")
(deftest szmc-sqlite-v0.1.0-schema-test
  (testing "create szmc v0.1.0 sqlite db from edn"
    (let [path "./test/fixture/test4.db" 
          file (io/as-file path)
          m    (edn/read-string (slurp edn-path))
          _    (create! path (:schema m))]
      (do (is (.exists file)) (.delete file)))))

;; TODO: test with running sql command(insert ...)
