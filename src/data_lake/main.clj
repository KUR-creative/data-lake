;map <F5> :wa<CR>:%Eval<CR>
;map <F6> :wa<CR>:!lein test<CR>
;map lr :wa<CR>:!lein run 
(ns data-lake.main
  (:require [data-lake.cli :as cli])
  (:gen-class))

(defn -main
  "Entry point"
  [& args]
  (apply cli/run args))
