With [datascript "0.16.1"] call to Datom/nth compiled with `{:direct-linking true}` will throw

`Exception in thread "main" java.lang.NoSuchMethodError: datascript.db$nth_datom.invokeStatic(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
	at datascript.db.Datom.nth(db.cljc:128)
	at clojure.lang.RT.nth(RT.java:868)
	at reproduce.core$test_case.invokeStatic(core.cljc:7)
	at reproduce.core$_main.invokeStatic(core.cljc:9)
	at reproduce.core$_main.doInvoke(core.cljc:9)
	at clojure.lang.RestFn.invoke(RestFn.java:397)
	at clojure.lang.AFn.applyToHelper(AFn.java:152)
	at clojure.lang.RestFn.applyTo(RestFn.java:132)
	at reproduce.core.main(Unknown Source)`

See https://clojure.org/reference/compilation#_compiler_options for the details about :direct-linking

cljc/reproduce/core.cljc contains both minimal test case and working example. Type hint for the first declaration makes the difference

Run `boot make && java -jar out/reproduce.jar` to reproduce
