(ns data-lake.sqlite-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]
            [data-lake.task.sqlite :refer :all]))

(deftest create!-test
  (testing "create sqlite db file"
    (let [path "./test/fixture/test.db" 
          file (io/as-file path)
          _    (create! path
                        (jdbc/create-table-ddl 
                          :file
                          [[:test :INTEGER]]))]
      (do (is (.exists file))
          (.delete file)
          ))))
