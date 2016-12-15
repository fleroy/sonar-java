class A {
  void foo() {
    double dt = 1.0;
    double sigma = 1.0;

    double dt2 = (1 / 2) * dt * dt * sigma * sigma; // Noncompliant
    double a = 1 / 2 * 5.0; // Noncompliant
  }
}
