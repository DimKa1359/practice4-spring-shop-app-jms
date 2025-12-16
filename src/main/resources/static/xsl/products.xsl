<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Products - REST API with XSLT</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    h1 { color: #333; }
                    table { border-collapse: collapse; width: 100%; margin-top: 20px; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    tr:nth-child(even) { background-color: #f9f9f9; }
                    .nav { margin: 20px 0; padding: 10px; background-color: #f5f5f5; border-radius: 5px; }
                    .nav a { margin-right: 15px; text-decoration: none; color: #0066cc; }
                    .nav a:hover { text-decoration: underline; }
                    .info { background-color: #e7f3fe; padding: 10px; border-left: 6px solid #2196F3; margin: 10px 0; }
                </style>
            </head>
            <body>
                <h1>Products List (XSLT Transformation)</h1>

                <div class="info">
                    This is an HTML representation of XML data using XSLT transformation.
                    Original XML: View source or use API clients.
                </div>

                <div class="nav">
                    <strong>API Endpoints:</strong>
                    <a href="/api/products">Products (XML)</a>
                    <a href="/api/products?format=json">Products (JSON)</a>
                    <a href="/api/categories">Categories (XML)</a>
                    <a href="/api/categories?format=json">Categories (JSON)</a>
                    <a href="/api/xslt-test/products">Test Products</a>
                    <hr/>
                    <strong>Web Interface:</strong>
                    <a href="/products">Products (Thymeleaf)</a>
                    <a href="/categories">Categories (Thymeleaf)</a>
                </div>

                <xsl:choose>
                    <xsl:when test="//product">
                        <!-- Если это список продуктов -->
                        <h2>Products (<xsl:value-of select="count(//product)"/> items)</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Price</th>
                                    <th>Category ID</th>
                                    <th>Category Name</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:for-each select="//product">
                                    <tr>
                                        <td><xsl:value-of select="id"/></td>
                                        <td><xsl:value-of select="name"/></td>
                                        <td>$<xsl:value-of select="format-number(price, '#,##0.00')"/></td>
                                        <td><xsl:value-of select="categoryId"/></td>
                                        <td><xsl:value-of select="categoryName"/></td>
                                        <td>
                                            <a href="/api/products/{id}">View XML</a> |
                                            <a href="/api/products/{id}?format=json">View JSON</a>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </tbody>
                        </table>
                    </xsl:when>
                    <xsl:when test="//category">
                        <!-- Если это список категорий -->
                        <h2>Categories (<xsl:value-of select="count(//category)"/> items)</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:for-each select="//category">
                                    <tr>
                                        <td><xsl:value-of select="id"/></td>
                                        <td><xsl:value-of select="name"/></td>
                                        <td>
                                            <a href="/api/categories/{id}">View XML</a> |
                                            <a href="/api/products?categoryId={id}">Products</a>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </tbody>
                        </table>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- Если это одиночный объект -->
                        <xsl:apply-templates select="*"/>
                    </xsl:otherwise>
                </xsl:choose>
            </body>
        </html>
    </xsl:template>

    <!-- Шаблон для одиночного продукта -->
    <xsl:template match="product">
        <h2>Product Details</h2>
        <table>
            <tr><th>ID:</th><td><xsl:value-of select="id"/></td></tr>
            <tr><th>Name:</th><td><xsl:value-of select="name"/></td></tr>
            <tr><th>Price:</th><td>$<xsl:value-of select="format-number(price, '#,##0.00')"/></td></tr>
            <tr><th>Category ID:</th><td><xsl:value-of select="categoryId"/></td></tr>
            <tr><th>Category Name:</th><td><xsl:value-of select="categoryName"/></td></tr>
        </table>
        <div class="nav">
            <a href="/api/products">Back to Products List</a>
        </div>
    </xsl:template>

    <!-- Шаблон для одиночной категории -->
    <xsl:template match="category">
        <h2>Category Details</h2>
        <table>
            <tr><th>ID:</th><td><xsl:value-of select="id"/></td></tr>
            <tr><th>Name:</th><td><xsl:value-of select="name"/></td></tr>
        </table>
        <div class="nav">
            <a href="/api/categories">Back to Categories List</a>
            |
            <a href="/api/products?categoryId={id}">View Products in this Category</a>
        </div>
    </xsl:template>

</xsl:stylesheet>