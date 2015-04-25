# Keynote Writer

## Instructions
1. Put the book in a text file titles the book's name + ".txt"
2. Put the title of the book on the first line of assignment.txt, exactly as it appears in step 1, but without the .txt. Put the author's last name on the next line.
3. put the text of the start line and end line of your current assignment. Leave them as "start" and "end" to have it start at the beginning and run to the end.
4. Put the page number in your copy of the book that your assignment started on, and the page that it ends on. (It will try to guess the page number in your book, but double check it if you want to be exact)
5. Put the focal points you want, one per line after that. Available focal points: Allusion, imagery of all forms, detail, figurative language (simile), setting, symbolism, and tone.
6. Run the KeynoteWriter.java file.
7. Fill out the remaining half of the keynotes that it brings up. If you do not like one, do not write anything and it will find another example of that focal point if it can.
8. Your keynotes should be located in a new file that it creates. Double check them to make sure they all look good.

## Change Log:

#### 1.01:
- Added feature to delete spaces at start of keynote
- Fixed bug that started keynotes with null
- Changed file read to "_assignment - Copy.txt"

#### 1.02:
- Fixes keynotes not to have duplicate spaces
- Added feature so if start line and end line are left, it will default them as the start or end

#### 1.03:
- Fixed a bug with the end sentence
- Added a page estimater

#### 1.04:
- Changed file read back to "assignment.txt"
- Using xml for word lists
- Edited README.md
- Added version printer

#### 1.05:
- Fixed error where titles such as "Dr." would count as an end of a sentance
- Put quotation marks around quotes 

#### 1.06:
- Added more variety to the output format of the quote analysis
- Added command line options for help and list available keynote types
- Added support for "all" keynote which includes all keynote types listed with the -l option
