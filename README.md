# CSV Normalizer

My submission is written in Java where I leveraged the OpenCSV library as well as Maven to manage building and running the script.

I included the maven wrapper so the project should be able to be build without installing Maven with the command


``./mvnw clean install``

As mentioned in the problem statement, there is a shell script that allows the script to be called

``./normalizer < sample.csv > output.csv
``

## Known Issues
I spent a good amount of time trying to triage an error I was getting from the custom converters that I used to handle the timezone conversion, timestamp to seconds elapsed conversion, and UTF-8 parsing

Unfortunately even after spending well over the suggested 4 hours reviewing library documentation trying to resolve the issue.

However, assuming I would be able to resolve that issue. I would make the following changes/updates as next steps

- Adding formal unit tests
- Accounting for much larger potential datasets and reading in the input file as a stream or iterator rather than reading it all in
  - To this point, streaming the input one by one would allow for being error handling and actually ensure invalid rows are omitted
- Create another converter to handle setting the Total Duration field more intelligently

