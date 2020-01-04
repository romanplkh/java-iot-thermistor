import React, { Component } from "react";
import "./error-boundary.css";

class ErrorBoundary extends Component {
  constructor() {
    super();

    this.state = {
      throwedError: false
    };
  }

  static getDerivedStateFromError(err) {
    return { throwedError: true };
  }

  componentDidCatch(error, details) {
    console.log(error);
  }

  render() {
    const { throwedError } = this.state;
    const errorPlaceHolder = (
      <div className="errorWrapper">
        <img
          className="errorImage"
          src="https://gallery.yopriceville.com/var/resizes/Free-Clipart-Pictures/Animals-PNG/Monkey_PNG_Image.png?m=1507172115"
          alt="errorImage"
        />
        <h1 className="errorMessage">
          There should be your chart, but something went wrong...
        </h1>
      </div>
    );

    return throwedError ? errorPlaceHolder : this.props.children;
  }
}

export default ErrorBoundary;
