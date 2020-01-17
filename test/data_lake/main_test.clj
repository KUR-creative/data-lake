(ns data-lake.main-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [data-lake.main :refer :all]))

(deftest -main-arg-test
  (testing "args"
    (is (= (with-out-str (-main)) help-msg))
    (is (= (with-out-str (-main "help")) help-msg)))
  (testing "sqlite create!"
    (let [db-path  "./test/fixture/test6.db"
          file     (io/as-file db-path)
          edn-path "./DB/sqlite/szmc_schema_0.1.0.edn"]
      (do (-main "sqlite" "new" db-path edn-path)
          (is (.exists file)) 
          (.delete file)))))

