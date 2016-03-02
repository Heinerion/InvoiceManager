# InvoiceManager
Fairly old project of mine to create invoices (per pdflatex)

_This is a port of an old, but still continued, project of mine, originally versioned using svn_

## Purpose
The main goal is to aid in creating invoices and letters for one or more companies.
Invoices and letters are created as latex-sources which are the processed by `pdflatex`.
The resulting documents are stored in the user's home directory, as well as the sources.

No database connections are implemented so far, just plain text files.
