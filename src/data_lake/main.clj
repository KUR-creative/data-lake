;map <F5> :wa<CR>:%Eval<CR>
;map <F8> :wa<CR>:!lein test<CR>
;map LR :wa<CR>:!lein run 
(ns data-lake.main
  (:require [data-lake.cli :as cli])
  (:gen-class))

(defn -main
  "Entry point"
  [& args]
  (apply cli/run args)
  (shutdown-agents)) ;; shutdown child processes..
