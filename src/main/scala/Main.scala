import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val spark=SparkSession.builder()
      .appName("Project Marketing Analysis")
      .master("local[1]")
      .getOrCreate()
    val df=spark.read.option("header",true).option("inferSchema",true).csv("Input/CapstoneMarketAnalysis.csv")

    df.show()
    df.printSchema()
    df.createOrReplaceTempView("mytable")
    val successnum=spark.sql("select count(*) as count from mytable where poutcome='success'")
    val failurenum=spark.sql("select count(*) as count from mytable where poutcome='failure'")
    successnum.show()
    val value1=successnum.first().getLong(0)
    println(successnum)
    val value2=failurenum.first().getLong(0)
    println(value2)
    failurenum.show()
    val totalnum=df.count()
    println(totalnum)
   val succerate=(value1.toDouble/totalnum)*100
    val failurerate=(value2.toDouble/totalnum)*100
    println("Sussess rate:  "+ succerate)
    println("Failure rate:  "+failurerate)
    val maximum=spark.sql("select max(age) as max_age from mytable")
    val minimum=spark.sql("select min(age) as min_age from mytable")
    val mean=spark.sql("select avg(age) as avg_age from mytable ")

    println("maximum age: "+maximum.first().getInt(0))
    println("minimum age: "+minimum.first().getInt(0))
    println("mean age: "+mean.first().getDouble(0))
    maximum.show()
    minimum.show()
    val avgBalance=spark.sql("select avg(balance) as avg_balance from mytable")
    avgBalance.show()
    println("avg balance: "+avgBalance.first().getDouble(0))
    val medianBalance=spark.sql( "SELECT  AVG(median_value) AS median" +
      "FROM (SELECT balance AS median_value, " +
      "ROW_NUMBER() OVER (ORDER BY balance  AS row_num," +
      "COUNT(*) OVER () AS total_rows FROM mytable) subquery" +
      " WHERE row_num IN (FLOOR((total_rows + 1) / 2), CEIL((total_rows + 1) / 2));"
    //println("median balance:  "+medianBalance.first().getDouble(0))
    //medianBalance.show()
    val ageMatter=spark.sql("select age,count(y) from mytable group by age order by age")
    ageMatter.show()
    val statusMatter=spark.sql("select marital,count(y) from mytable group by marital")
    statusMatter.show()
    val age_marital = spark.sql("select age, marital, count(*) as number from mytable where y='yes' group by age,marital order by number desc ").show()







  }
}