import Document, { Html, Head, Main, NextScript } from 'next/document';

class MyDocument extends Document {
  render() {
    return (
      <Html lang="en">
        <Head>
          {/* Favicon */}
          <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png" />
          <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png" />
          <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png" />
          <link rel="manifest" href="/site.webmanifest" />

          {/* Charset */}
          <meta charSet="UTF-8" />

          {/* Viewport */}
          <meta name="viewport" content="width=device-width, initial-scale=1" />

          {/* Meta Description */}
          <meta
            name="description"
            content="Welcome to Axle Logistics Kenya, your go-to platform for reliable transportation services."
          />

          {/* Keywords */}
          <meta
            name="keywords"
            content="logistics, hauling, delivery, transport, Kenya, jobs, Axle Logistics Kenya, transport kenya, Logistics Services Kenya"
          />

          {/* Author */}
          <meta name="author" content="Axle Logistics Kenya" />

          {/* Open Graph Meta Tags */}
          <meta property="og:title" content="Axle Logistics Kenya" />
          <meta
            property="og:description"
            content="We provide logistics services across Kenya."
          />
          <meta property="og:url" content="https://axle-ke.co.ke" />
          <meta property="og:type" content="website" />
          <meta property="og:image" content="https://axle-ke.co.ke/og-image.jpg" />

          {/* Twitter Card Meta Tags */}
          <meta name="twitter:card" content="summary_large_image" />
          <meta name="twitter:title" content="Axle Logistics Kenya" />
          <meta
            name="twitter:description"
            content="We provide unmatched logistic solutions across Kenya."
          />
          <meta name="twitter:image" content="https://axle-ke.co.ke/twitter-image.jpg" />
          <meta name="twitter:site" content="@yourTwitterHandle" />

          {/* Google Analytics / Tag Manager */}
          <script async src="https://www.googletagmanager.com/gtag/js?id=AW-11300700833"></script>
          <script
            dangerouslySetInnerHTML={{
              __html: `
                window.dataLayer = window.dataLayer || [];
                function gtag(){dataLayer.push(arguments);}
                gtag('js', new Date());
                gtag('config', 'AW-11300700833');
              `,
            }}
          />
          <meta name="google-site-verification" content="ETOurN9lrhjPZaSS-Wh-aR48snAK8tqorwWMnX1SxLY" />
        </Head>
        <body>
          <Main />
          <NextScript />
        </body>
      </Html>
    );
  }
}

export default MyDocument;
