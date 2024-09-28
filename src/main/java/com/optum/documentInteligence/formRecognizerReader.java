package com.optum.documentInteligence;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.*;
import com.azure.core.credential.AzureKeyCredential;

public class formRecognizerReader {

    public static void main(String[] args) {
        String endpoint = "";
        String key = "";
        AzureKeyCredential credential = new AzureKeyCredential(key);
        DocumentAnalysisClient client = new DocumentAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .buildClient();

        String fileUri = "";
        String content = "";
        AnalyzeResult result = client.beginAnalyzeDocumentFromUrl("prebuilt-document", fileUri)
                .getFinalResult();
        for (DocumentKeyValuePair kvp : result.getKeyValuePairs()) {
            if (kvp.getValue() == null) {
                System.out.println("  Found key with no value: '" + kvp.getKey().getContent() + "'");
            } else {
                content=content+ kvp.getKey().getContent()+" : "+kvp.getValue().getContent()+"\n";
                //System.out.println("  Found key-value pair: '" + kvp.getKey().getContent() + "' and '" + kvp.getValue().getContent() + "'");
            }
        }

        System.out.print(content);

       /* for (DocumentPage page : result.getPages()) {
            System.out.println("Document Page " + page.getPageNumber() + " has " + page.getLines().size() + " line(s), " + page.getWords().size() + " word(s),");
            System.out.println("and " + page.getSelectionMarks().size() + " selection mark(s).");

            for (int i = 0; i < page.getLines().size(); i++) {
                DocumentLine line = page.getLines().get(i);
                System.out.println("  Line " + i + " has content: '" + line.getContent() + "'.");

                System.out.println("    Its bounding box is:");
                System.out.println("      Upper left => X: " + line.getBoundingPolygon().get(0).getX() + ", Y= " + line.getBoundingPolygon().get(0).getY());
                System.out.println("      Upper right => X: " + line.getBoundingPolygon().get(1).getX() + ", Y= " + line.getBoundingPolygon().get(1).getY());
                System.out.println("      Lower right => X: " + line.getBoundingPolygon().get(2).getX() + ", Y= " + line.getBoundingPolygon().get(2).getY());
                System.out.println("      Lower left => X: " + line.getBoundingPolygon().get(3).getX() + ", Y= " + line.getBoundingPolygon().get(3).getY());
            }

            for (int i = 0; i < page.getSelectionMarks().size(); i++) {
                DocumentSelectionMark selectionMark = page.getSelectionMarks().get(i);

                System.out.println("  Selection Mark " + i + " is " + selectionMark.getSelectionMarkState() + ".");
                System.out.println("    Its bounding box is:");
                System.out.println("      Upper left => X: " + selectionMark.getBoundingPolygon().get(0).getX() + ", Y= " + selectionMark.getBoundingPolygon().get(0).getY());
                System.out.println("      Upper right => X: " + selectionMark.getBoundingPolygon().get(1).getX() + ", Y= " + selectionMark.getBoundingPolygon().get(1).getY());
                System.out.println("      Lower right => X: " + selectionMark.getBoundingPolygon().get(2).getX() + ", Y= " + selectionMark.getBoundingPolygon().get(2).getY());
                System.out.println("      Lower left => X: " + selectionMark.getBoundingPolygon().get(3).getX() + ", Y= " + selectionMark.getBoundingPolygon().get(3).getY());
            }
        }*/

        /*for (DocumentStyle style : result.getStyles()) {
            // Check the style and style confidence to see if text is handwritten.
            // Note that value '0.8' is used as an example.

            boolean isHandwritten = style.isHandwritten() != null && style.isHandwritten();

            if (isHandwritten && style.getConfidence() > 0.8) {
                System.out.println("Handwritten content found:");

                *//*for (DocumentSpan span : style.getSpans()) {
                    System.out.println("  Content: " + result.getContent().substring(span.getIndex(), span.getIndex() + span.getLength()));
                }*//*
            }
        }

        System.out.println("The following tables were extracted:");
        for (int i = 0; i < result.getTables().size(); i++) {
            DocumentTable table = result.getTables().get(i);
            System.out.println("  Table " + i + " has " + table.getRowCount() + " rows and " + table.getColumnCount() + " columns.");

            for (DocumentTableCell cell : table.getCells()) {
                System.out.println("    Cell (" + cell.getRowIndex() + ", " + cell.getColumnIndex() + ") has kind '" + cell.getKind() + "' and content: '" + cell.getContent() + "'.");
            }
        }*/
    }
}